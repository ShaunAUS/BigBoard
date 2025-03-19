dependencies {
    implementation(project(":storage:db"))
    implementation(project(":common:snowflake"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    // 트랜잭션 사용을 위한 추가
    implementation("org.springframework:spring-tx:${property("springTxVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

}
