import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;

public class testesr {
	public static void main(String[] args) throws IOException, JAXBException {
		Marshaller marshaller = JAXBContext.newInstance(TesterElement.class).createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		marshaller.marshal(new TesterElement(), new File("C:\\Temp\\temp.xml"));
		
		Runtime.getRuntime().exec("cmd /c START C:\\Temp\\temp.xml");
	}

	@XmlRootElement
	public static class TesterElement {
		String valueWithCarriageReturn = "some\rvalue";
		String valueWithNewLine = "some\nvalue";
		String valueWithBoth = "some\r\nvalue";
		String otherCharacters = "some < | > | & value";

		public String getOtherCharacters() {
			return otherCharacters;
		}

		public void setOtherCharacters(String otherCharacters) {
			this.otherCharacters = otherCharacters;
		}

		public String getValueWithCarriageReturn() {
			return valueWithCarriageReturn;
		}

		public void setValueWithCarriageReturn(String valueWithCarriageReturn) {
			this.valueWithCarriageReturn = valueWithCarriageReturn;
		}

		public String getValueWithNewLine() {
			return valueWithNewLine;
		}

		public void setValueWithNewLine(String valueWithNewLine) {
			this.valueWithNewLine = valueWithNewLine;
		}

		public String getValueWithBoth() {
			return valueWithBoth;
		}

		public void setValueWithBoth(String valueWithBoth) {
			this.valueWithBoth = valueWithBoth;
		}
	}
}
