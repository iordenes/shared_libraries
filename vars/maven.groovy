def call(){
    allStages()
}

def allStages(){
    stageCleanBuild()
    stageCleanTest()
    stagePackage()
    stageSonar()
    stageUploadNexus()
}

def stageCleanBuild(){
    stage("Paso 1: Build && Test"){
        sh "echo 'Build && Test!'"
        sh "mvn clean compile -e"
        // code
    }
}

def stageCleanTest(){
    stage("Paso 2: Testear"){
        sh "echo 'Test Code!'"
        // Run Maven on a Unix agent.
        sh "mvn clean test -e"
    }
}

def stagePackage(){
    stage("Paso 3: Build .Jar"){
        sh "echo 'Build .Jar!'"
        // Run Maven on a Unix agent.
        sh "mvn clean package -e"
    }
}

def stageSonar(){
    stage("Paso 4: Análisis SonarQube"){
        // steps {
            withSonarQubeEnv('sonarqube') {
            sh "echo 'Calling sonar Service in another docker container!'"
            // Run Maven on a Unix agent to execute Sonar.
            sh "echo '${env.GIT_URL}'_'${env.GIT_BRANCH}'_'${env.BUILD_NUMBER}'"
            sh "echo 'RAMA: ['${env.GIT_BRANCH}'] EJECUCIÓN NR:[' ${env.BUILD_NUMBER}']"
            //sh 'mvn clean verify sonar:sonar -Dsonar.projectKey='${env.GIT_URL}'_'${env.GIT_BRANCH}'_'${env.BUILD_NUMBER}' -Dsonar.projectName=REPO:[' ${env.GIT_URL} '] RAMA: ['${env.GIT_BRANCH}'] EJECUCIÓN NR:[' ${env.BUILD_NUMBER}']'
            }
        // }
    }
    // post {
    //     //record the test results and archive the jar file.
    //     success {
    //         //archiveArtifacts artifacts:'build/*.jar'
    //         nexusPublisher nexusInstanceId: 'nexus',
    //         nexusRepositoryId: 'devops-usach-nexus',
    //         packages: [
    //             [$class: 'MavenPackage',
    //             mavenAssetList: [
    //                 [classifier: '',
    //                 extension: '.jar',
    //                 filePath: 'build/DevOpsUsach2020-0.0.1.jar']
    //                 ],
    //                 mavenCoordinate: [
    //                     artifactId: 'DevOpsUsach2020',
    //                     groupId: 'cl.devopsusach2020',
    //                     packaging: 'jar',
    //                     version: '0.0.1-feature-nexus']
    //             ]
    //         ]
    //     }
    // }
}

def stageUploadNexus(){
    stage("Paso 5: Subir Nexus"){
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class: 'MavenPackage',
                mavenAssetList: [
                    [classifier: '',
                    extension: 'jar',
                    filePath: 'build/LaboratorioM3-ID-0.0.1.jar'
                ]
            ],
                mavenCoordinate: [
                    artifactId: 'LaboratorioM3-ID',
                    groupId: 'com.laboratorioM3',
                    packaging: 'jar',
                    version: '0.0.1'
                ]
            ]
        ]
    }
}

return this;