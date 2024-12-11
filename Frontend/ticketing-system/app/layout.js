import { Toaster } from "@/components/ui/toaster";
import "./globals.css";
import { UserProvider } from './../UserContext';

export const metadata = {
  title: "BOOK IT | Find, Book, and Enjoy – It’s That Simple!",
  description: "Generated by create next app",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body
        className={`bg-bdark`}
      >
        <UserProvider>
        {children}
        <Toaster/>
        </UserProvider>
      </body>
    </html>
  );
}
