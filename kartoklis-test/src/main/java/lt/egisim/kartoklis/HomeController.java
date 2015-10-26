package lt.egisim.kartoklis;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// result files array
	private File[] results;

	// return home page
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homeHandler() {
		return "uploadMultiple";
	}

	// download result files
	@RequestMapping(value = "/resultsAG.txt", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody FileSystemResource downloadAG() {
		return new FileSystemResource(results[0]);
	}

	@RequestMapping(value = "/resultsHN.txt", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody FileSystemResource downloadHN() {
		return new FileSystemResource(results[1]);
	}

	@RequestMapping(value = "/resultsOU.txt", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody FileSystemResource downloadOU() {
		return new FileSystemResource(results[2]);
	}

	@RequestMapping(value = "/resultsVZ.txt", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody FileSystemResource downloadVZ() {
		return new FileSystemResource(results[3]);
	}

	/**
	 * Upload multiple files using Spring Controller
	 */
	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
	public String uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files) {
		try {
			results = FileWorker.findRepeatingWords(files);
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}
		return "results";
	}
}
