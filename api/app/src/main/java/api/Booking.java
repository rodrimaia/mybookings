package api;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
    private String phone;
    @DatabaseField()
    @NonNull
    private String name;
    @DatabaseField()
    @NonNull
    private String email;
    @DatabaseField()
    private long checkIn;
    @DatabaseField()
    private long checkOut;

}
