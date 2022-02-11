FROM eclipse-temurin:17.0.2_8-jre

COPY maven/target/*.jar /app.jar
COPY maven/entrypoint.sh /entrypoint.sh


RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chmod +x /entrypoint.sh
USER appuser

CMD ["/entrypoint.sh"]