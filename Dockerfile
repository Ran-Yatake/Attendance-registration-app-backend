# ビルド用イメージとしてEclipse Temurin JDK 21を使用
FROM eclipse-temurin:21-jdk AS build

# 作業ディレクトリを/appに設定
WORKDIR /app

# プロジェクトの全ファイルをコンテナの/appにコピー
COPY . .

# Maven Wrapperでテストをスキップしてビルド（mvnwに実行権限が必要）
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# 実行用イメージとしてEclipse Temurin JRE 21を使用
FROM eclipse-temurin:21-jre

# 作業ディレクトリを/appに設定
WORKDIR /app

# ビルド成果物（jarファイル）を実行用イメージにコピー
COPY --from=build /app/target/*.jar app.jar

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "app.jar"]
