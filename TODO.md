# PartyTime Server - TODO List

## 🔴 High Priority

### Input Validation & Error Handling
- [x] Add validation annotations to DropRequest DTO (@Min, @Max, @Positive, @NotBlank)
- [x] Add @Valid annotation to DropsController endpoint
- [ ] Create GlobalExceptionHandler with @RestControllerAdvice for validation errors
- [ ] Add exception handling for general errors in GlobalExceptionHandler

### Transaction Management
- [x] Add @Transactional to DropService.handleDrop() method
- [x] Add @Transactional to DropService.cleanupInactiveParties() method

### Database Optimization
- [ ] Add database indexes to DropParty entity (world/is_active, is_active/last_drop_at)

---

## 🟡 Medium Priority

### Configuration Management
- [ ] Move timeout/cleanup constants to application.properties
- [ ] Add @ConfigurationProperties or @Value to load configuration from properties file

### API Improvements
- [ ] Add pagination support to GET /api/party endpoint
- [ ] Add Swagger/OpenAPI dependency and configure API documentation

### Testing
- [ ] Write unit tests for duplicate detection logic
- [ ] Write unit tests for party timeout logic
- [ ] Write unit tests for average drop calculation

---

## 🔒 Security & Authentication

### Spring Security Setup
- [ ] Add Spring Security dependency to build.gradle
- [ ] Create User entity and UserRepository for authentication
- [ ] Configure SecurityFilterChain with JWT or OAuth2
- [ ] Add authentication endpoints (login, register, refresh token)

### Endpoint Security
- [ ] Secure /api/drops endpoint - require authentication
- [ ] Secure /api/party endpoints with role-based access (ADMIN for cleanup)
- [ ] Add API key authentication option for game client integration

**Recommended Approach:**
- API Keys for game clients (simple, stateless)
- JWT tokens for admin/web dashboard
- Roles: `ROLE_CLIENT` (game clients) and `ROLE_ADMIN` (cleanup access)

---

## 🟢 Nice to Have

### Infrastructure & Operations
- [ ] Add CORS configuration if using browser-based client
- [ ] Add rate limiting to prevent API spam
- [ ] Create Dockerfile for containerization
- [ ] Add health check endpoint for monitoring

---

## 📝 Notes

### Current Architecture
- **Request Flow:** HTTP Request → Controller → Service → Repository → Database
- **Duplicate Detection:** 0.5 second window
- **Party Timeout:** 5 minutes of inactivity
- **Cleanup Schedule:** Runs every 2 minutes automatically

### API Endpoints
- `POST /api/drops` - Report a drop from client
- `GET /api/party` - Get all active drop parties
- `POST /api/party/cleanup` - Manually trigger cleanup of inactive parties

### Future Considerations
- Consider separating PartyService from DropService for better separation of concerns
- Add metrics/monitoring (Prometheus, Grafana)
- Add logging aggregation (ELK stack)
- Consider caching for frequently accessed active parties
