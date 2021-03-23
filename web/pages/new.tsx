import "react-date-range/dist/styles.css"; // main style file
import "react-date-range/dist/theme/default.css"; // theme css file
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Head from "next/head";
import Header from "../components/header";
import BookingForm from "../components/BookingForm";

export default function Details() {
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
        <BookingForm />
        <ToastContainer />
      </main>
    </div>
  );
}

