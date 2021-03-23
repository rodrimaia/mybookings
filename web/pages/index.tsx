import { format } from "date-fns";
import { InferGetStaticPropsType } from "next";
import Head from "next/head";
import { useRouter } from "next/router";
import Header from '../components/header'

import type { Booking } from "../types";
import useSWR from "swr";
import axios from "axios";

type ApiResponse = {
  data: { data: Booking[] };
};

export async function getStaticProps() {
  const res = await fetch('http://localhost:3001/bookings');
  const response: ApiResponse = await res.json();

  return {
    props: {
      bookings: response.data,
    },
    // Next.js will attempt to re-generate the page:
    // - When a request comes in
    // - At most once every second
    revalidate: 5, // In seconds
  };
}

export default function Home() {
  const router = useRouter();

  const { data, error } = useSWR("http://localhost:3001/bookings", axios.get)

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
        <div className="md:container md:mx-auto">
          <div className="my-5 md:mx-8 bg-white rounded-lg px-5 py-5 shadow ">
            <h2 className="text-xl font-bold mb-5 "> Bookings List </h2>

            <table className="w-full text-left ">
              <thead>
              <tr className="uppercase text-xs text-gray-500 mb-5">
                <th> Costumer</th>
                <th> Phone</th>
                <th> Reserved</th>
                <th> Guests</th>
              </tr>
              </thead>
              <tbody>
              {(data?.data?.data as Booking[])?.map((m) => (
                <tr
                  key={m.id}
                  onClick={() => router.push(`/${m.id}`)}
                  className="text-sm hover:bg-gray-300"
                  style={{ lineHeight: "3rem" }}
                >
                  <td> {m.name}</td>
                  <td> {m.phone}</td>
                  <td>
                      <span className="font-bold color-primary">
                        {" "}
                        {format(new Date(m.checkIn), "do LLL")}
                      </span>
                    &#160;to&#160;
                    <span className="font-bold color-primary">
                        {" "}
                      {format(new Date(m.checkOut), "do LLL")}
                      </span>
                  </td>
                  <td> {m.guests} </td>
                </tr>
              ))}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  );
}
