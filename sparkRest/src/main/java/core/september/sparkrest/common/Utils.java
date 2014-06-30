package core.september.sparkrest.common;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import core.september.sparkrest.annotation.Controller;
import core.september.sparkrest.controller.BaseController;
import core.september.sparkrest.data.DataStore;

public class Utils {
	
	//ReaderUtil readerUtil = new ReaderUtil(new GenericObjectPool<StringBuffer>(new StringBufferFactory()));

	public static Utils INSTANCE = new Utils();
	
	private Utils() {
		
	}
	
	public static String handleThrowable(Throwable t,Response res) {
		res.status(505);
		//res.body(t.getMessage());
		return t.getMessage();
	}
	
	
	
	public  String digest(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance(Constants.DIGEST_ALG);
			return new String(md.digest(input.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			LoggerFactory.getLogger(Utils.class).error(e.getMessage());
			return input;
		}
	}
	
	public <T> BasicDAO<T, ObjectId> getDao(Class<T> instanceClass) {
		return new BasicDAO<T, ObjectId>(instanceClass, DataStore.INSTANCE.getStore());
	}
	
	public Callable<String> getController(String path, Request req) throws Exception {
		ArrayList<Class<?>> controllers = getClassesForPackage(BaseController.class.getPackage());
		for(Class<?> clz: controllers) {
			for(Annotation annotation: clz.getAnnotations()) {
				if(annotation.annotationType().equals(Controller.class)) {
					for(String anPath : ((Controller)annotation).path()) {
						if(path.equalsIgnoreCase(anPath)) {
							Constructor constructor = clz.getConstructor(Request.class);
							return (Callable<String>) constructor.newInstance(req);
						}
					}
				}
			}
		}
		throw new Exception("No such controller for path: "+path);
	}
	
	private static ArrayList<Class<?>> getClassesForPackage(Package pkg) {
	    String pkgname = pkg.getName();
	    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
	    // Get a File object for the package
	    File directory = null;
	    String fullPath;
	    String relPath = pkgname.replace('.', '/');
	    System.out.println("ClassDiscovery: Package: " + pkgname + " becomes Path:" + relPath);
	    URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
	    System.out.println("ClassDiscovery: Resource = " + resource);
	    if (resource == null) {
	        throw new RuntimeException("No resource for " + relPath);
	    }
	    fullPath = resource.getFile();
	    System.out.println("ClassDiscovery: FullPath = " + resource);

	    try {
	        directory = new File(resource.toURI());
	    } catch (URISyntaxException e) {
	        throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
	    } catch (IllegalArgumentException e) {
	        directory = null;
	    }
	    System.out.println("ClassDiscovery: Directory = " + directory);

	    if (directory != null && directory.exists()) {
	        // Get the list of the files contained in the package
	        String[] files = directory.list();
	        for (int i = 0; i < files.length; i++) {
	            // we are only interested in .class files
	            if (files[i].endsWith(".class")) {
	                // removes the .class extension
	                String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);
	                System.out.println("ClassDiscovery: className = " + className);
	                try {
	                    classes.add(Class.forName(className));
	                } 
	                catch (ClassNotFoundException e) {
	                    throw new RuntimeException("ClassNotFoundException loading " + className);
	                }
	            }
	        }
	    }
	    else {
	        try {
	            String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
	            JarFile jarFile = new JarFile(jarPath);         
	            Enumeration<JarEntry> entries = jarFile.entries();
	            while(entries.hasMoreElements()) {
	                JarEntry entry = entries.nextElement();
	                String entryName = entry.getName();
	                if(entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
	                    System.out.println("ClassDiscovery: JarEntry: " + entryName);
	                    String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
	                    System.out.println("ClassDiscovery: className = " + className);
	                    try {
	                        classes.add(Class.forName(className));
	                    } 
	                    catch (ClassNotFoundException e) {
	                        throw new RuntimeException("ClassNotFoundException loading " + className);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
	        }
	    }
	    return classes;
	}
}
