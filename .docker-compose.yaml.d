version: "3"
services:
  database:
    image: postgres:latest
    restart: always
    ports:
      - 5432:5432
    container_name: team-voided-website-db
    environment:
      - POSTGRES_USER=ender
      - POSTGRES_PASSWORD=ender
      - POSTGRES_DB=team-voided-website-db