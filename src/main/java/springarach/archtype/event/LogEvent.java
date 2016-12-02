package springarach.archtype.event;

import java.io.Serializable;
import java.util.Date;

public class LogEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private String source;

	private Date timestamp;

	private String message;
	
	Object o;

	public Object getO() {
		return o;
	}

	public void setO(Object o) {
		this.o = o;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "LogEvent [source=" + source + ", timestamp=" + timestamp
				+ ", message=" + message + "]";
	}


}
