package lt.egisim.kartoklis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.multipart.MultipartFile;


// Reads file and uses wordMap as word counter
public class FileWorkerThread extends Thread {

	private ConcurrentHashMap<String, Integer> wordMap;
	private MultipartFile file;

	public FileWorkerThread(MultipartFile file, ConcurrentHashMap<String, Integer> wordMap) {
		this.file = file;
		this.wordMap = wordMap;
	}

	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine())!= null){
				StringTokenizer st = new StringTokenizer(line, " ");
                while(st.hasMoreTokens()){
                    String tmp = st.nextToken().toLowerCase();
                    if(wordMap.containsKey(tmp)){
                        wordMap.put(tmp, wordMap.get(tmp)+1);
                    } else {
                        wordMap.put(tmp, 1);
                    }
                }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
