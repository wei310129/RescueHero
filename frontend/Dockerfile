# Multi-stage build for Vue.js frontend
# Build stage
FROM node:18-bullseye-slim AS builder
WORKDIR /app

# Install necessary build tools (to avoid node-gyp/native package issues on alpine/musl)
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    python3 \
    ca-certificates \
    git \
  && rm -rf /var/lib/apt/lists/*

# Copy only package files to leverage Docker cache
COPY package*.json ./

# Install exact dependencies using npm ci (for CI/production use)
RUN npm ci --no-audit --prefer-offline

# Copy source and build
COPY . .
ENV NODE_ENV=production
RUN npm run build

# Runtime stage
FROM nginx:stable-alpine as runtime

# Remove default nginx content
RUN rm -rf /usr/share/nginx/html/*

# Copy built assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose HTTP port
EXPOSE 80

# Use default nginx entrypoint
CMD ["nginx", "-g", "daemon off;"]
