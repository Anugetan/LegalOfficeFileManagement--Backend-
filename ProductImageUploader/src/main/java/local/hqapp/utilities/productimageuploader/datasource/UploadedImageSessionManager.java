package local.hqapp.utilities.productimageuploader.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadedImageSessionManager {

	private static UploadedImageSessionManager instance; 
	
	public static synchronized UploadedImageSessionManager getInstance() {
		
		if (instance == null) {
			instance = new UploadedImageSessionManager();
		}
		
		return instance;
	}
	
	private List<UploadedImageSession> uploadedImageSessions;
	private AtomicInteger idGenerator;
	
	private UploadedImageSessionManager() {
		idGenerator = new AtomicInteger();
	}
	
	public synchronized boolean  add(UploadedImageSession u) {
		
		if (uploadedImageSessions == null) {
			uploadedImageSessions = new ArrayList<UploadedImageSession>();
		}
		
		u.setId(idGenerator.incrementAndGet());
		
		return uploadedImageSessions.add(u);
	}
	
	public synchronized boolean remove(UploadedImageSession u) {
		
		if (uploadedImageSessions != null) {
			return uploadedImageSessions.remove(u);
		}
		
		return false;
	}
	
	public synchronized UploadedImageSession get(int id) {
		
		if (uploadedImageSessions != null) {
			for (UploadedImageSession u : uploadedImageSessions) {
				if (u.getId() == id) {
					return u;
				}
			}
		}
		
		return null;
	}
}
