

count = 0

encodings = ['utf-8', 'latin-1', 'cp1252']
javaFiles = []

new File("src").eachDirRecurse(){dir -> dir.eachFileMatch(~/.*.java/) { file -> javaFiles.add(file) }}

println suggestFile(javaFiles.find { file ->  countMistakes(file,'utf-8') > 0  }) 
countAllMistakes()

def suggestFile(file){
    ficheroTemp = new File("/home/imediava/Escritorio/temp/despues.java")
    if (false && countMistakes(file, 'cp1252') == 0){
       
        ficheroTemp.delete()
        ficheroTemp << getFicheroConEncoding(file, 'cp1252')
        """/home/imediava/Programas-StandAlone/eclipse/eclipse ${file.getPath()}""".execute()
        """/home/imediava/Programas-StandAlone/eclipse/eclipse /home/imediava/Escritorio/temp/despues.java""".execute()
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        println "Aceptar(y/n):"
        String input = br.readLine()
        if (input == "y"){
            """cp /home/imediava/Escritorio/temp/despues.java ${file.getAbsolutePath()}"""
            return ""
        }
    }
    
    ficheroTemp.delete()
    ficheroTemp << reemplazarAMano(file)
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
    println "Aceptar(y/n):"
    String input = br.readLine()
    if (input == "y"){
            println "A cambiarlo: ${file.getAbsolutePath()}"
            """cp /home/imediava/Escritorio/temp/despues.java ${file.getAbsolutePath()}""".execute()
            return ""
    }
    
}

def reemplazarAMano(file){
     salida = ""
     println "A modificar: $file\n"
     new File(file.getPath()).getText('utf-8').getChars().each {
                 if (it == "\uFFFD"){
                      BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
                      println ""
                      println "Sustituir por:"
                      String input = br.readLine()
                      salida = salida + input 
                 }else{
                     salida = salida + it
                     print it
                       
                 }      
                 
     }
     return salida
}

def getFicheroConEncoding(file, encoding){
    return new File(file.getPath()).getText(encoding)
}

def countAllMistakes(){
    mistakes = 0
    new File("src").eachDirRecurse(){dir -> dir.eachFileMatch(~/.*.java/) { mistakes += countMistakes(it, 'utf-8') }}
    println "Total Mistakes: $mistakes"
    javaFiles = []
    new File("src").eachDirRecurse(){dir -> dir.eachFileMatch(~/.*.java/) { file -> javaFiles.add(file) } }
    numClasses = javaFiles.findAll { file ->  countMistakes(file,'utf-8') > 0  }.size()
    println "Classes with mistakes: $numClasses"
    
}

def countMistakes(file, encoding){
    totalClase = 0
    new File(file.getPath()).getText(encoding).getChars().each {                        
                 if (it == "\uFFFD"){
                        totalClase ++
                 }
    }     
    return totalClase 
                 
}
 
//println "Salida:$salida"
