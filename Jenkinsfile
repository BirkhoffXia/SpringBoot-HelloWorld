pipeline {
    agent any

		environment {
			harborUser = '*******'
			harborPaswd = '********'
			harborAddress = 'registry.cn-hangzhou.aliyuncs.com'
			harborRepo = 'birkhoff'
		}
    stages {
        stage('[1]-拉去Git仓库代码') {
            steps {
		checkout scmGit(branches: [[name: '${tag}']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/BirkhoffXia/SpringBoot-HelloWorld.git']])
                echo '[1]-拉去Git仓库代码 - SUCCESS'
            }
        }
        stage('[2]-通过MAVEN构建项目') {
            steps {
		sh '/usr/local/mvn/bin/mvn clean package -DskipTests'    
                echo '[2]-通过MAVEN构建项目 - SUCCESS'
            }
        }
        stage('[3]-通过SonarQube代码质量检测') {
            steps {
		sh '/usr/local/sonar-scanner/bin/sonar-scanner -Dsonar.projectname=${JOB_NAME} -Dsonar.projectKey=${JOB_NAME} -Dsonar.java.binaries=./target -Dsonar.source=./ -Dsonar.login=admin -Dsonar.password=sheca '
                echo '[3]-通过SonarQube代码质量检测 - SUCCESS'
            }
        }
        stage('[4]-通过Docker制作自定义镜像') {
            steps {
		sh '''mv ./target/*.jar ./deploy
docker build -t ${JOB_NAME}:${tag} ./deploy/'''
		echo '[4]-通过Docker制作自定义镜像 - SUCCESS'
            }
        }
        stage('[5]-将自定义镜像推送到Aliyun') {
            steps {
		    sh '''docker login --username=${harborUser} --password=${harborPaswd} ${harborAddress}
docker tag ${JOB_NAME}:${tag}  ${harborAddress}/${harborRepo}/${JOB_NAME}:${tag}
docker push ${harborAddress}/${harborRepo}/${JOB_NAME}:${tag}'''
                echo '[5]-将自定义镜像推送到Aliyun - SUCCESS'
            }
        }    
        stage('[6.6]-通过Publish Over SSH通知目标服务器') {
            steps {
		sshPublisher(publishers: [sshPublisherDesc(configName: 'test', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "deploy.sh ${harborAddress} ${harborRepo} ${JOB_NAME} ${tag} ${host_port}  ${container_port}", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
		echo '[6]-通过Publish Over SSH通知目标服务器 - SUCCESS'
            }
        }     
    }
	post {
		success{
			dingtalk(
				robot: 'Jenkins-DingDing',
				type: 'MARKDOWN',
				title: "success: ${JOB_NAME}",
				text: ["- 成功构建: ${JOB_NAME}! \n- 版本: ${tag} \n- 持续时间: ${currentBuild.durationString}"]
			)
		}
		failure{
			dingtalk(
				robot: 'Jenkins-DingDing',
				type: 'MARKDOWN',
				title: "success: ${JOB_NAME}",
				text: ["- 构建失败: ${JOB_NAME}! \n-版本: ${tag} \n-持续时间: ${currentBuild.durationString}"]
			)
		}
	}
}
