package dynamicrefactoring.plugin.xml.classifications.imp;

import dynamicrefactoring.plugin.xml.classifications.XmlClassificationsReader;

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
