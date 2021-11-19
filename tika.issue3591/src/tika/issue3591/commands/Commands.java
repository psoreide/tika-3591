package tika.issue3591.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tika.issue3591.Extract;
import tika.issue3591.TextExtractor;

@Component(immediate = true, service = Commands.class, property = {
		Commands.COMMAND_SCOPE + "=tika", Commands.COMMAND_FUNCTION + "=testpdf"
})
public class Commands {

	public static final String	COMMAND_SCOPE		= "osgi.command.scope";
	public static final String	COMMAND_FUNCTION	= "osgi.command.function";

	private static Logger logger = LoggerFactory.getLogger(Commands.class);
	
	@Reference
	private TextExtractor extractor;

	public void testpdf() {
		
		logger.debug("Executing testpdf...");
		
		Bundle bundle = FrameworkUtil.getBundle(Commands.class);
		URL docURL = bundle.getEntry("testdocs/intro-linux.pdf");
		
		try (InputStream is = docURL.openStream()) {
			Extract extract = extractor.extractText("application/pdf", is);
			logger.info("Extract: " + extract);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
