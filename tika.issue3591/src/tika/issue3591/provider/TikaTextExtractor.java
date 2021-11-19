package tika.issue3591.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MediaTypeRegistry;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.Parser;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tika.issue3591.Extract;
import tika.issue3591.TextExtractor;

@Component
public class TikaTextExtractor implements TextExtractor {

	private static final Logger log = LoggerFactory.getLogger(TikaTextExtractor.class);

	private Tika tika;
	private Detector detector;
	private Parser parser;
	private MediaTypeRegistry registry;

	@Activate
	public void activate() {
		MimeTypes types = MimeTypes.getDefaultMimeTypes();
		registry = types.getMediaTypeRegistry();
		tika = new Tika(detector, parser);
	}

	@Override
	public String[] getMimeTypes() {
		SortedSet<String> result = new TreeSet<>();
		for (MediaType mediaType : registry.getTypes()) {
			result.add(mediaType.toString());
		}
		return result.toArray(new String[result.size()]);
	}

	@Override
	public Extract extractText(String mimeType, InputStream inputStream) throws IOException {
		Metadata metadata = new Metadata();
		metadata.add(Metadata.CONTENT_TYPE, mimeType);
		try {
			String text = tika.parseToString(inputStream, metadata);
			Extract result = new Extract();
			result.setText(text);
			StringBuilder sb = new StringBuilder();
			for (String metakey : metadata.names()) {
				String[] datas = metadata.getValues(metakey);
				sb.setLength(0);
				String sep = "";
				for (String data :datas) {
					sb.append(sep);
					sb.append(data);
					sep = ", ";
				}
				result.addMetadata(metakey, sb.toString());
			}
			return result;
		} catch (Throwable e) {
			log.error("Tika text extractor is unable to extract from file of type " + mimeType);
			throw new IOException(e);
		}
	}

	protected Detector getDetector() {
		return detector;
	}

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY)
	protected void setDetector(Detector detector) {
		this.detector = detector;
	}

	protected Parser getParser() {
		return parser;
	}

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY)
	protected void setParser(Parser parser) {
		this.parser = parser;
	}
}
