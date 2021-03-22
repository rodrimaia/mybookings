package api;

import static spark.Spark.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import spark.RouteGroup;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "bookings")
class Booking {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField()
    private int guests;
    @DatabaseField()
    @NonNull
    private String name;
    @NonNull
    @DatabaseField()
    private Date initialDate;
    @DatabaseField()
    @NonNull
    private Date endDate;

}

public class App {
    public static void main(String[] args) {

        try {

            Gson gson = new Gson();
            String databaseUrl = "jdbc:h2:mem:account";
            ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
            Dao<Booking, Integer> bookingsDao =
                    DaoManager.createDao(connectionSource, Booking.class);

            TableUtils.createTable(connectionSource, Booking.class);

            Booking b1 = new Booking(0, 1, "Rodrigo", new Date(), new Date());
            Booking b2 = new Booking(0, 2, "rodrigo's doppelganger", new Date(), new Date());

            bookingsDao.create(b1);
            bookingsDao.create(b2);

            path("/bookings", () -> {

                get("", (req, res) -> {
                    return bookingsDao.queryForAll();
                });

                get("/:id", (req, res) -> {
                    return bookingsDao.queryForId(Integer.parseInt(req.params("id")));
                });

/*
            get("/email/:email") { req, res ->
                    userDao.findByEmail(req.params("email"))
            }
*/

//            post("/create", (req, res) -> {
//                    userDao.save(name = req.qp("name"), email = req.qp("email"))
//                res.status(201)
//                "ok"
//            })
//
//            patch("/update/:id", (req, res) -> {
//                    userDao.update(
//                            id = req.params("id").toInt(),
//                            name = req.qp("name"),
//                            email = req.qp("email")
//                    )
//                "ok"
//            })
//
//            delete("/delete/:id") { req, res ->
//                    userDao.delete(req.params("id").toInt())
//                "ok"
//            }

            }, gson::toJson);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
