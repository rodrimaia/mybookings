import axios from "axios";
import { Form, Formik } from "formik";
import { useRouter } from "next/router";
import { DateRange } from "react-date-range";
import { toast } from "react-toastify";

import { Input } from "./Input";
import { Booking } from "../types";

type BookingFormType = {
  booking?: Booking;
};

export default function BookingForm({ booking }: BookingFormType) {
  const router = useRouter();
  const isUpdating = booking?.id;

  const emptyBooking: Booking = {
    name: "",
    email: "",
    phone: "",
    guests: 1,
    checkIn: new Date().getTime(),
    checkOut: new Date().getTime()
  };

  return (
    <div
      className=" bg-white rounded-lg px-5 py-5 shadow mx-auto w-full md:w-auto "
      style={{ maxWidth: "440px" }}
    >
      <>
        <h2 className="text-xl font-bold mb-5 "> New Booking </h2>
        <Formik
          initialValues={isUpdating ? booking : emptyBooking}
          validate={(values) => {
            const errors = {} as any;
            if (!values.email) {
              errors.email = "Required";
            } else if (
              !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(values.email)
            ) {
              errors.email = "Invalid email address";
            } else if (!values.checkIn || !values.checkOut) {
              errors.checkIn = "Missing checkin/out dates";
            } else if (values.guests < 1) {
              errors.guests = "Reservation must have at least 1 guest";
            }
            return errors;
          }}
          onSubmit={(values, { setSubmitting }) => {
            let response;

            if (isUpdating) {
              response = axios.put(
                `http://localhost:3001/bookings/${booking.id}`,
                values
              );
            } else {
              response = axios.post("http://localhost:3001/bookings", values);
            }

            response
              .then(() => {
                toast.success("Success!");
                setTimeout(() => {
                  router.push("/");
                }, 1000);
              })
              .catch((error) => {
                toast.error(error.message);
              });
          }}
        >
          {({ isSubmitting, setFieldValue, values }) => (
            <Form>
              <Input label="Name" name="name" elementType="text" />
              <Input label="Email" name="email" elementType="email" />

              <label className="block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2">
                CheckIn Dates
              </label>
              <DateRange
                minDate={new Date()}
                ranges={[
                  {
                    startDate: new Date(values["checkIn"]),
                    endDate: new Date(values["checkOut"]),
                    key: "selection"
                  }
                ]}
                onChange={(ranges) => {
                  console.log(ranges);
                  setFieldValue(
                    "checkIn",
                    ranges.selection.startDate.getTime()
                  );
                  setFieldValue("checkOut", ranges.selection.endDate.getTime());
                }}
              />

              <div className="flex">
                <Input label="Phone" name="phone" elementType="phone" />

                <div className="ml-1">
                  <Input
                    label="Number of Guests"
                    name="guests"
                    elementType="number"
                  />
                </div>
              </div>

              <div className="pt-4 flex items-center space-x-4">
                <button
                  className={`flex justify-center items-center w-full text-gray-900 font-bold px-4 py-3 rounded-md focus:outline-none ${!isUpdating && "hidden"}`}
                  onClick={() => {
                    axios
                      .delete(`http://localhost:3001/bookings/${booking.id}`)
                      .then(() => {
                        toast.info("Booking Removed!");
                        setTimeout(() => {
                          router.push("/");
                        }, 1000);
                      });
                  }}
                >
                  Delete Booking
                </button>
                <button
                  disabled={isSubmitting}
                  className="bg-primary font-bold flex justify-center items-center w-full text-white px-4 py-3 rounded-md focus:outline-none"
                >
                  {isUpdating ? "Update" : "Create"}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </>
    </div>
  );
}
