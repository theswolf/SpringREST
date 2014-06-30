package core.september.sparkrest.common;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

public abstract class ObjectPool<T> {
	  private long expirationTime;

	  private Hashtable<T, Long> locked, unlocked;

	  public ObjectPool() {
	    expirationTime = TimeUnit.MINUTES.toMillis(Constants.POOL_EXPIRE_TIME);
	    locked = new Hashtable<T, Long>();
	    unlocked = new Hashtable<T, Long>();
	  }

	  protected abstract T create();

	  public abstract boolean validate(T o);

	  public abstract void expire(T o);

	  public synchronized T checkOut() {
	    long now = System.currentTimeMillis();
	    T t;
	    if (unlocked.size() > 0) {
	      Enumeration<T> e = unlocked.keys();
	      while (e.hasMoreElements()) {
	        t = e.nextElement();
	        if ((now - unlocked.get(t)) > expirationTime) {
	          // object has expired
	          unlocked.remove(t);
	          expire(t);
	          t = null;
	        } else {
	          if (validate(t)) {
	            unlocked.remove(t);
	            locked.put(t, now);
	            return (t);
	          } else {
	            // object failed validation
	            unlocked.remove(t);
	            expire(t);
	            t = null;
	          }
	        }
	      }
	    }
	    // no objects available, create a new one
	    t = create();
	    locked.put(t, now);
	    return (t);
	  }

	  public synchronized void checkIn(T t) {
	    locked.remove(t);
	    unlocked.put(t, System.currentTimeMillis());
	  }
	}
