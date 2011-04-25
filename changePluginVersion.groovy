import groovy.xml.XmlUtil

class PluginVersionChanger {

	def static main(String[] args){
		assert args.length == 1;
		def newVersion = args[0]
		changeVersionManifestFile("./dynamicrefactoring.plugin/META-INF/MANIFEST.MF",newVersion)
		changeVersionManifestFile("./dynamicrefactoring.tests/META-INF/MANIFEST.MF",newVersion)
		modifyRootPomFile("pom.xml", newVersion)
		modifyFeatureFile("./dynamicrefactoring.feature/feature.xml", newVersion)
		modifyPom("./dynamicrefactoring.feature/pom.xml", newVersion)
		modifyPom("./dynamicrefactoring.targetplatform/pom.xml", newVersion)
		modifyPom("./dynamicrefactoring.plugin/pom.xml", newVersion)
		modifyPom("./dynamicrefactoring.tests/pom.xml", newVersion)
		modifyPom("./dynamicrefactoring.p2repository/pom.xml", newVersion)

	}

	def changeVersionManifestFile(filePath, version){
	    def newFileContent = ""
	    new File(filePath).eachLine { line ->
		linea = line
		if (line =~ /Bundle-Version:/ ){
		    match = line =~ /Bundle-Version: (\d+\.\d+\.\d+)/
		    linea = linea.replace(match[0][1], version)
		}
		newFileContent += linea + "\n"
	    }
	    new File(filePath).delete()
	    new File(filePath) << newFileContent
	}

	def modifyRootPomFile(filePath, newVersion){
	    
	    def root = new XmlParser().parseText(new File(filePath).text)
	    root.version[0].value = newVersion
	    root.build[0].plugins[0].plugin
		                .find { it.artifactId[0].text() == 'target-platform-configuration'}
		                .configuration[0].target[0].artifact[0].version[0].value = newVersion
	    
	    new File(filePath).delete()
	    new File(filePath) << XmlUtil.serialize(root)
	}




	def modifyFeatureFile(filePath, newVersion){

	    def root = new XmlParser().parseText(new File(filePath).text)
	    root.@version = newVersion
	    root.plugin[0].@version = newVersion
	    XmlUtil.serialize(root)
	    
	    new File(filePath).delete()
	    new File(filePath) << XmlUtil.serialize(root)
	}


	def modifyPom(filePath, newVersion){

	    def root = new XmlParser().parseText(new File(filePath).text)
	    root.parent[0].version[0].value = newVersion
	    root.version[0].value= newVersion
	    
	    new File(filePath).delete()
	    new File(filePath) << XmlUtil.serialize(root)
	}


}
