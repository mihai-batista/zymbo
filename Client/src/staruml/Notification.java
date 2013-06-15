package staruml;

public class Notification {
	private ElementType type;
	private String content;
	
	public Notification(ElementType iType, String iContent) {
		type = iType;
		content = iContent;
	}

	public ElementType getType() {
		return type;
	}

	public void setType(ElementType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
