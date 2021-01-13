package bavard.db;

import bavard.chat.*;
import bavard.user.User;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class MessageStoreDatabase implements MessageStore {

    private static MessageStoreDatabase instance = null;
    private Connection connection = null;

    public static synchronized MessageStore getInstance() {
        if (instance == null) {
            instance = new MessageStoreDatabase();
            instance.init();
        }
        return instance;
    }

    // Get connection to the database or create one if it does not already exist
    private void init() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:store.db");

            String sql = "CREATE TABLE IF NOT EXISTS messages ("
                    + "sender_uid text,"
                    + "recipient_uid text,"
                    + "datetime text,"
                    + "type text,"
                    + "content text"
                    + ")";

            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            // TODO: what to do if this fails?
        }
    }

    @Override
    public void saveMessage(TextMessage msg) throws SQLException {
        String sql = "INSERT INTO messages (sender_uid, recipient_uid, datetime, type, content) VALUES (?,?,?,?,?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, msg.getSender().getUid());
        pstmt.setString(2, msg.getRecipient().getUid());
        pstmt.setString(3, msg.getDatetime());
        pstmt.setString(4, "text");
        pstmt.setString(5, msg.getText());

        pstmt.execute();

        System.out.println("You have new message(s) from "+msg.getFrom().getPseudonym());
    }

    @Override
    public void saveMessage(ImageMessage msg) {
        // TODO: save ImageMessage to database
    }

    @Override
    public void saveMessage(DocumentMessage msg) {
        // TODO: save DocumentMessage to database
    }

    @Override
    public ArrayList<Message> getMessagesBetween(User user1, User user2) throws SQLException {
        ArrayList<Message> messageHistory = new ArrayList<>();

        String sql = "SELECT sender_uid, recipient_uid, datetime, type, content FROM messages "
                + "WHERE ((sender_uid = ? AND recipient_uid = ?) OR (sender_uid = ? AND recipient_uid = ?))";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user1.getUid());
        pstmt.setString(2, user2.getUid());
        pstmt.setString(3, user2.getUid());
        pstmt.setString(4, user1.getUid());

        ResultSet rs = pstmt.executeQuery();

        // Parse results and instantiate each message to the correct type, then add to message history
        while (rs.next()) {
            User sender = ChatSession.getInstance().getUserByUid(rs.getString("sender_uid"));
            User recipient = ChatSession.getInstance().getUserByUid(rs.getString("recipient_uid"));

            switch (rs.getString("type")) {
                case "text":
                    TextMessage tm = new TextMessage(
                            sender,
                            recipient,
                            OffsetDateTime.parse(rs.getString("datetime")),
                            rs.getString("content")
                    );

                    messageHistory.add(tm);
                    break;
                case "image":
                    // TODO: add ImageMessage to message history
                    break;
                case "document":
                    // TODO: add DocumentMessage to message history
                    break;
                default:
                    break;
            }
        }

        return messageHistory;
    }
}
