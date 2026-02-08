# QA Log — Campus Companion

1. Issue: App crashed when creating event with empty title  
   Fix: Added input validation for empty title

2. Issue: Images were not displayed correctly in feed  
   Fix: Added Coil AsyncImage with proper contentScale

3. Issue: Feed was reloading entire list on pagination  
   Fix: Implemented paginated loading with page size = 20

4. Issue: Search returned empty screen without feedback  
   Fix: Added "No results" empty state

5. Issue: Favorites were lost after restart  
   Fix: Stored favorites in Firebase Realtime Database
