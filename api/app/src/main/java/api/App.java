package api;

import static spark.Spark.*;

import com.google.gson.Gson;
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
import spark.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Data
@AllArgsConstructor
abstract class Response {
    private int code;
}

@Data
class SuccessResponse extends Response {
    private Object data;
    private int code;

    public SuccessResponse(Object data, int code) {
        super(code);
        this.data = data;
    }
}

@Data
class ErrorResponse extends Response {
    private String message;
    private int code;

    public ErrorResponse(String message, int code) {
        super(code);
        this.message = message;
    }
}

public class App {

    public static void main(String[] args) {

        try {

            port(3001);
            enableCORS("*", "GET, PUT, POST, DELETE", "Content-Type");
            Gson gson = new Gson();
            String databaseUrl = "jdbc:h2:mem:bookings";
            ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
            Dao<Booking, Integer> bookingsDao =
                    DaoManager.createDao(connectionSource, Booking.class);

            BookingService bookingService = new BookingService(bookingsDao);

            setupDB(connectionSource, bookingsDao);

            options("/", (req, res) -> {
                res.status(200);
                return "ok";
            });

            path("/bookings", () -> {

                get("", (req, res) -> {
                    List<Booking> bookings = bookingService.getAll();
                    return new SuccessResponse(bookings, 200);
                }, gson::toJson);

                get("/:id", (req, res) -> {
                    Booking booking = null;
                    try {
                        booking = bookingService.getById(Integer.parseInt(req.params("id")));
                    } catch (BookingNotFoundException e) {
                        res.status(400);
                        return new ErrorResponse("booking not found", 404);
                    }
                    return new SuccessResponse(booking, 200);
                }, gson::toJson);

                post("", (req, res) -> {
                    Booking booking = gson.fromJson(req.body(), Booking.class);
                    try {
                        booking.setId(bookingService.create(booking));

                        res.status(201);
                        return new SuccessResponse(booking, 201);
                    } catch (BookingNotFoundException e) {
                        res.status(400);
                        return new ErrorResponse("booking not found", 404);
                    } catch (OverlappingBookingsException e) {
                        res.status(400);
                        return new ErrorResponse("you sent a overlapped booking", 400);
                    }
                }, gson::toJson);

                put("/:id", (req, res) -> {
                    Booking booking = gson.fromJson(req.body(), Booking.class);
                    try {
                        bookingService.update(booking);
                        res.status(201);
                        return new SuccessResponse(booking, 201);
                    } catch (BookingNotFoundException e) {
                        res.status(400);
                        return new ErrorResponse("booking not found", 404);
                    } catch (OverlappingBookingsException e) {
                        res.status(400);
                        return new ErrorResponse("you sent a overlapped booking", 400);
                    }
                }, gson::toJson);

                delete("/:id", (req, res) -> {
                    int id = Integer.parseInt(req.params("id"));
                    try {
                        bookingService.delete(id);
                        res.status(204);
                        return new SuccessResponse("ok", 204);
                    } catch (BookingNotFoundException e) {
                        res.status(400);
                        return new ErrorResponse("booking not found", 404);
                    }
                }, gson::toJson);
                ;
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setupDB(ConnectionSource connectionSource, Dao<Booking, Integer> bookingsDao) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Booking.class);

        Booking b1 = new Booking(0, 1, "+55 31 9999999", "Rodrigo", "rodrigo.maia.pereira@gmail.com", new Date().getTime(), new Date().getTime());
        //Booking b2 = new Booking(0, 2, "+55 31 9999999", "rodrigo's doppelganger", "rodrigo@yahoo.com", new Date().getTime(), new Date().getTime());

        bookingsDao.create(b1);
        //bookingsDao.create(b2);
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
