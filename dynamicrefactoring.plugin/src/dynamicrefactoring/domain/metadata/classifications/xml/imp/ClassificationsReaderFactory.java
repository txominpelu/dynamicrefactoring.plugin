package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import dynamicrefactoring.domain.metadata.classifications.xml.XmlClassificationsReader;

class ClassificationsReaderFactory {
	
	public enum ClassificationsReaderTypes{
		JAXB_READER
		{
			@Override
			protected XmlClassificationsReader getReader() {
				return JAXBClassificationsReader.getInstance();
			}
		}	,	
		JDOM_READER
		
		{
			@Override
			protected XmlClassificationsReader getReader() {
				return JDOMClassificationsReader.getInstance();
			}
		};
		
		protected abstract XmlClassificationsReader getReader();
	}
	
	public static XmlClassificationsReader getReader(ClassificationsReaderTypes tipo){
		return tipo.getReader();
	}

}
