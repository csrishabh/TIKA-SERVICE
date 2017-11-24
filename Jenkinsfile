def pipeline

node {
    // Set Variables needed by template
    env.githubRepoUrl="git@us2-github-1.adminsys.mrll.com:Javelin/ocr-tika-service.git"
    env.pcfAppName="ocr-tika-service"
    env.jarGroupId="com.mrll.javelin"
    env.jarArtifactId="ocr-tika-service"
    env.sonarBuildName="default"
    env.sonarBuildName="skipsonar"
    env.hasAcceptanceTests=false

    // Fetch and execute template
    git url: "git@us2-github-1.adminsys.mrll.com:Javelin/jenkins-templates.git"
    pipeline = load 'jenkins-pipeline-tools.groovy'
}

pipeline.microservicePipeline()