package api;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.Where;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BookingService {

    private Dao<Booking, Integer> bookingDao;

    public List<Booking> getAll() throws SQLException {
        return bookingDao.queryForAll();
    }

    public Booking getById(int id) throws SQLException, BookingNotFoundException {
        validateBooking(id);
        return bookingDao.queryForId(id);
    }

    public int create(Booking booking) throws OverlappingBookingsException, SQLException, BookingNotFoundException {

        validateOverlapping(booking);

        return bookingDao.create(booking);
    }


    public int update(Booking booking) throws OverlappingBookingsException, SQLException, BookingNotFoundException {
        validateBooking(booking.getId());
        validateOverlapping(booking);

        return bookingDao.update(booking);
    }

    public int delete(int id) throws SQLException, BookingNotFoundException {
        validateBooking(id);
        return bookingDao.deleteById(id);
    }

    private void validateOverlapping(Booking booking) throws SQLException, OverlappingBookingsException {
        Where<Booking, Integer> where = bookingDao.queryBuilder().where();
        where.between("checkin", booking.getCheckIn(), booking.getCheckOut());
        where.or();
        where.between("checkout", booking.getCheckIn(), booking.getCheckOut());
        where.and();
        where.ne("id", booking.getId());
        PreparedQuery<Booking> preparedQuery = where.prepare();

        List<Booking> overlapped = bookingDao.query(preparedQuery);

        if (overlapped.size() > 0) {
            throw new OverlappingBookingsException();
        }
    }

    private void validateBooking(int id) throws SQLException, BookingNotFoundException {
        Booking booking = bookingDao.queryForId(id);

        if (booking.getId() < 1) {
            throw new BookingNotFoundException();
        }
    }
}
