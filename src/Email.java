
import java.util.List;

/**
 * Created by Coder on 2017/4/16.
 */
public class Email {
    public static String TEXT = "text/plain;charset=utf-8";
    public static String HTML = "text/html;charset=utf-8";

    private String toAddress;
    private String subject;
    private String content;
    private String contentType;
    private List<String> MimeBodyParts;

    @Override
    public String toString() {
        return "Email{" +
                "toAddress='" + toAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", contentType='" + contentType + '\'' +
                ", MimeBodyParts=" + MimeBodyParts.toString() +
                '}';
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getMimeBodyParts() {
        return MimeBodyParts;
    }

    public void setMimeBodyParts(List<String> mimeBodyParts) {
        MimeBodyParts = mimeBodyParts;
    }
}
