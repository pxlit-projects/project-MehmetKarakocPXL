FROM nginx

COPY nginx.conf /etc/nginx/nginx.conf

COPY ./dist/frontend-web/browser /usr/share/nginx/html

EXPOSE 80

# Stage 1: Build the Angular application
# FROM node:18-alpine as build

# WORKDIR /usr/src/app

# # Copy package.json and package-lock.json to leverage caching
# COPY package*.json ./

# # Install dependencies
# RUN npm install

# # Copy the rest of the application
# COPY . .

# # Build the Angular application for production
# RUN npm run build --configuration=production

# # Stage 2: Serve the application with nginx
# FROM nginx:latest

# # Copy the custom nginx configuration
# COPY nginx.conf /etc/nginx/nginx.conf

# # Copy the Angular build output from the build stage
# COPY --from=build /usr/src/app/dist/frontend-web/browser /usr/share/nginx/html

# # Expose port 80
# EXPOSE 80

# # Start nginx
# CMD ["nginx", "-g", "daemon off;"]
