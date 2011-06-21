#!/bin/bash

fecha=20110620
plugin_version=3.1.6
#ruta_proyecto=/home/imediava/Escritorio/workspace-proyecto2/dynamicrefactoring.plugin/

#cd $ruta_proyecto 
groovy changePluginVersion.groovy $plugin_version

mvn clean install

if [ "$?" = "0" ]; then
	cp "dynamicrefactoring.p2repository/target/dynamicrefactoring.p2repository.zip" "/home/imediava/Escritorio/PruebasProgramacion/mercurial-repo/refactoring-plugin/"
	cp "dynamicrefactoring.p2repository/target/dynamicrefactoring.p2repository.zip" "/home/imediava/Escritorio/PruebasProgramacion/mercurial-repo/refactoring-plugin/dynamicrefactoring.p2repository-$plugin_version-$fecha.zip"
	cd /home/imediava/Escritorio/PruebasProgramacion/mercurial-repo/refactoring-plugin/
	echo "Llego"
	hg add .
	hg commit -m "Version $plugin_version."
	hg push 
	echo "Lo subio"

fi
