FROM nginx:latest
LABEL maintainer="Барсуков Максим Андреевич"
WORKDIR /usr/share/nginx/html
COPY index.html .
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
