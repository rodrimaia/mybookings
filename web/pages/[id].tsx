import "react-date-range/dist/styles.css"; // main style file
import "react-date-range/dist/theme/default.css"; // theme css file
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import axios from "axios";
import Head from "next/head";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

import type { Booking } from "../types";
import Header from "../components/header";
import BookingForm from "../components/BookingForm";

export default function Details() {
  const router = useRouter();
  const { id } = router.query;
  const [booking, setBooking] = useState<Booking>();


  useEffect(() => {
    if (id) {
      axios.get(`http://localhost:3001/bookings/${id}`).then((data) => {
        setBooking(data.data.data);
      });
    }
  }, [id]);

  return (
    <div>
      <Head>
        <link rel="preconnect" href="https://fonts.gstatic.com" />
        <link
          href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100;300;400;700&display=swap"
          rel="stylesheet"
        />
        <link
          href="https://fonts.googleapis.com/icon?family=Material+Icons"
          rel="stylesheet"
        />
        <title>My Bookings</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main>
        <Header />
        {booking &&
        <BookingForm booking={booking} />
        }
        <ToastContainer />
      </main>
    </div>
  );
}

