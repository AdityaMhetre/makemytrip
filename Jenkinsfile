pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '3'))
    }

    tools {
        maven 'maven_3.9.11'
    }

    stages {
        stage('Code Compilation') {
            steps {
                echo 'Starting Code Compilation now...'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed Successfully!'
            }
        }
        stage('Code QA Execution') {
            steps {
                echo 'Running JUnit Test Cases...'
                sh 'mvn clean test'
                echo 'JUnit Test Cases Completed Successfully !'
            }
        }
        stage('SonarQube Code Quality') {
                    steps {
                        echo 'Starting SonarQube Code Quality Scan...'

                        withSonarQubeEnv('sonar-server') {
                            sh '''
                              mvn clean verify \
                              org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594:sonar
                            '''
                        }
                        echo 'SonarQube Scan Completed. Checking Quality Gate...'
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                        echo 'Quality Gate Check Completed!'
                    }
        }
        stage('Code Package') {
            steps {
                echo 'Creating WAR Artifact...'
                sh 'mvn clean package'
                echo 'WAR Artifact Created Successfully!'
            }
        }
        stage('Build & Tag Docker Image') {
                    steps {
                        echo 'Building Docker Image with Tags...'
                        sh "docker build -t adityamhetre/makemytrip:1.1 -t makemytrip:1.1 ."
                        echo 'Docker Image Build Completed!'
                    }
        }
        stage('Docker Image Scanning') {
                    steps {
                        echo 'Scanning Docker Image with Trivy...'
                        sh 'trivy image ${DOCKER_IMAGE}:1.1 || echo "Scan Failed - Proceeding with Caution"'
                        echo 'Docker Image Scanning Completed!'
                    }
                }
        stage('Push Docker Image to Docker Hub') {
                    steps {
                        script {
                            withCredentials([string(credentialsId: 'dockerhubCred', variable: 'dockerhubCred')]) {
                                sh 'docker login docker.io -u adityamhetre -p ${dockerhubCred}'
                                echo 'Pushing Docker Image to Docker Hub...'
                                sh 'docker push adityamhetre/makemytrip:1.1'
                                echo 'Docker Image Pushed to Docker Hub Successfully!'
                            }
                        }
                    }
                }
        stage('Push Docker Image to Amazon ECR') {
                    steps {
                        script {
                            withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: "https://343474957259.dkr.ecr.ap-south-1.amazonaws.com"]) {
                                echo 'Tagging and Pushing Docker Image to ECR...'
                                sh '''
                                    docker images
                                    docker tag makemytrip:latest 343474957259.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:1.1
                                    docker push 343474957259.dkr.ecr.ap-south-1.amazonaws.com/makemytrip:1.1
                                '''
                                echo 'Docker Image Pushed to Amazon ECR Successfully!'
                            }
                        }
                    }
        }
        stage('Upload Docker Image to Nexus') {
                    steps {
                        script {
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                sh 'docker login http://13.200.138.246:8085/repository/makemytrip/ -u admin -p admin123'
                                echo "Push Docker Image to Nexus : In Progress"
                                sh 'docker tag makemytrip 13.200.138.246:8085/makemytrip:1.1'
                                sh 'docker push 13.200.138.246:8085/makemytrip'
                                echo "Push Docker Image to Nexus : Completed"
                            }
                        }
                    }
                }

    }
}



