package tika.issue3591;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Extract {

	private Map<String, String> metadata;
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addMetadata(String key, String value) {
		if (metadata == null) {
			metadata = new HashMap<>();
		}
		metadata.put(key, value);
	}

	public Map<String, String> getMetadata() {
		if (metadata == null) {
			return Collections.emptyMap();
		}
		return metadata;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Metadata:");
		sb.append("=========");
		getMetadata().entrySet().forEach(me -> {
			sb.append(me.getKey());
			sb.append("  ");
			sb.append(me.getValue());
			sb.append('\n');
		});
		sb.append('\n');
		sb.append("Text:");
		sb.append("=====");
		sb.append(text);
		
		return sb.toString();
	}
}
