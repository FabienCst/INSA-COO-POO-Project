## Checklist

**Purpose:** To have an overview of what is done and what still needs doing.

### Application

- App
  - [x] Start-up sequence
  - [ ] Notify of your absence when shutting down
  - [ ] Make it possible to run several clients on the same computer (see <1> under User)
- MainController
  - [ ] Start-up sequence
- NetworkController
  - [ ] Start-up sequence
- User interface (console)
  - [ ] Request pseudonym until a valid one is given
  - [ ] Show a proper CLI interface (run in loop, show menu of choices, ...)
  - [ ] Get list of active users
- User interface (graphical) _coming soon to a screen near you ..._
- User
  - [ ] Change pseusonym
  - [ ] <1> Abstract the User's address to be identified differently depending on the environment (live network vs. localhost)
- Chat
  - [ ] Accept an incoming chat connection
  - [ ] Start an active chat session
  - [ ] End an active chat session
  - [ ] Receive a text message
  - [ ] Send a text message
- Network
  - [ ] Respond to a "Who is out there?" request
  - [ ] Handle a notification of presence
  - [ ] Handle a notification of absence
- Data store
  - [ ] Add and configure SQLite database
  - [ ] Implement MessageStore interface for the database

### Project

- Docs
  - [x] Update use case diagram
  - [x] Update class diagram
  - [x] Update sequence diagrams
  - [ ] Update composite structure diagrams
  - [ ] Update state diagrams
  - [ ] Enrich the readme
- Deployment
  - [ ] Create an easy deployment option (gradle, sample database)
  - [ ] Detail how to deploy for testing (readme)
- Tests _hah!_
- Coursework
  - [ ] Report
- Specifications
  - [ ] Check application against formal specifications
