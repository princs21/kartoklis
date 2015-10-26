package lt.egisim.kartoklis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.web.multipart.MultipartFile;

public class FileWorker {

	public static File[] findRepeatingWords(MultipartFile[] files) throws Exception {
		// thread pool is for optimal number of threads running at the same time
		ExecutorService threadPool = new ThreadPoolExecutor(4, // core
				// size
				Integer.MAX_VALUE, // max size
				60L, // idle timeout
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()); // queue
																		// without
																		// size
		// thread pool executor adds additional functionality to thread pool
		ThreadPoolExecutor pool = (ThreadPoolExecutor) threadPool;

		File[] results = new File[4];
		ConcurrentHashMap<String, Integer> wordMap = new ConcurrentHashMap<String, Integer>();
		
		//let treads do the file reading
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isEmpty()) {
				try {
					threadPool.submit(new FileWorkerThread(files[i], wordMap));
				} catch (Exception e) {
					throw (e);
				}
			}
		}

		// wait until all threads finish
		while (pool.getActiveCount() > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw (e);
			}
		}

		// generate and write to result files
		try {
			File resultsAG = File.createTempFile("Results A-G.txt", null);
			File resultsHN = File.createTempFile("Results H-N.txt", null);
			File resultsOU = File.createTempFile("Results O-U.txt", null);
			File resultsVZ = File.createTempFile("Results V-Z.txt", null);
			PrintWriter writerAG = new PrintWriter(resultsAG);
			PrintWriter writerHN = new PrintWriter(resultsHN);
			PrintWriter writerOU = new PrintWriter(resultsOU);
			PrintWriter writerVZ = new PrintWriter(resultsVZ);

			for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
				if (entry.getKey().substring(0, 1).compareToIgnoreCase("a") >= 0
						&& entry.getKey().substring(0, 1).compareToIgnoreCase("g") <= 0) {
					writerAG.println(entry.getKey() + " " + entry.getValue());
				} else if (entry.getKey().substring(0, 1).compareToIgnoreCase("n") <= 0) {
					writerHN.println(entry.getKey() + " " + entry.getValue());
				} else if (entry.getKey().substring(0, 1).compareToIgnoreCase("u") <= 0) {
					writerOU.println(entry.getKey() + " " + entry.getValue());
				} else if (entry.getKey().substring(0, 1).compareToIgnoreCase("z") <= 0) {
					writerVZ.println(entry.getKey() + " " + entry.getValue());
				}
			}

			writerAG.close();
			writerHN.close();
			writerOU.close();
			writerVZ.close();

			results[0] = resultsAG;
			results[1] = resultsHN;
			results[2] = resultsOU;
			results[3] = resultsVZ;

		} catch (FileNotFoundException e) {
			throw (e);
		} catch (IOException e) {
			throw (e);
		}
		return results;
	}

}
