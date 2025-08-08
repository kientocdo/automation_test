# Sử dụng Node.js image chính thức từ Docker Hub
FROM node:16

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Copy package.json và package-lock.json vào thư mục làm việc
COPY package*.json ./

# Cài đặt các dependencies cho ứng dụng
RUN npm install

# Copy toàn bộ mã nguồn vào trong container
COPY . .

# Cấu hình môi trường
ENV DB_HOST=mysql
ENV DB_USER=root
ENV DB_PASSWORD=rootuser
ENV DB_NAME=profileDB

# Mở cổng 3000 cho ứng dụng
EXPOSE 3000

# Chạy ứng dụng khi container khởi động
CMD ["node", "server.js"]
