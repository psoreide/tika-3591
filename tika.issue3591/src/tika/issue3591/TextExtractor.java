package tika.issue3591;

import java.io.IOException;
import java.io.InputStream;

public interface TextExtractor {

	String[] getMimeTypes();

	Extract extractText(String mimeType, InputStream inputStream) throws IOException;

}
