import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api", // your Spring Boot backend
});

export default api;
