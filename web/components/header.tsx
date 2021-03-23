import Link from "next/link";

export default function Header() {
  return <div className="flex items-center">
    <h1 className="my-5 mx-8 text-4xl font-bold">
      {" "}
      <span>My</span>
      <span className="color-primary"><Link href="/">Bookings</Link></span>
    </h1>
    <span className="my-5 mx-8  font-bold"> <Link href="/new">NEW BOOKING </Link></span>
  </div>;
}