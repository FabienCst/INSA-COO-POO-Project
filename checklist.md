## Checklist

**Purpose:** To have an overview of what is done and what still needs doing.

### Application

- App
  - [x] Start-up sequence
  - [x] Notify of your absence when shutting down
  - [x] Make it possible to run several clients on the same computer
- MainController
  - [ ] Start-up sequence
- NetworkController
  - [x] Start-up sequence
- User interface (console)
  - [x] Request pseudonym until a valid one is given
  - [x] Show a proper CLI interface (run in loop, show menu of choices, ...)
  - [x] Get list of active users
- User interface (graphical) _coming soon to a screen near you ..._
- User
  - [x] Change pseudonym
- Chat
  - [ ] Accept an incoming chat connection
  - [ ] Start an active chat session
  - [ ] End an active chat session
  - [ ] Receive a text message
  - [ ] Send a text message
- Network
  - [x] Respond to a "Who is out there?" request
  - [x] Handle a notification of presence
  - [x] Handle a notification of absence
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
