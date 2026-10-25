<!-- Prompt A run on parcelpoint. AI tool: Claude (Cowork), model claude-opus-5-5, 25 October 2026. The four Java files were attached. -->

1. Files read: parcelpoint/src/main/java/parcelpoint/Locker.java, Main.java, Parcel.java, ParcelService.java

2. Findings

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | 1 | ParcelService.java:42 | Strings compared with `==` (`parcel.getCode() == code`). The code typed at the keyboard is a different String object, so `pickup` never finds the parcel and always returns fee 0. | Must fix | Compare with `parcel.getCode().equals(code)`. |
| 2 | 1 | ParcelService.java:76 | `FileWriter` is never closed or flushed, so parcels.csv stays empty and the file handle leaks. | Must fix | Open the writer in a try-with-resources statement. |
| 3 | 1 | ParcelService.java:80-81 | Empty catch block hides every write error. | Must fix | Log or report the `IOException`. |
| 4 | 1 | ParcelService.java:6 | Unused import `java.util.Date`. | Should fix | Remove the import. |
| 5 | 1 | ParcelService.java:12 | Unused private field `maxParcels`. | Should fix | Remove the field, or enforce the limit in `storeParcel`. |
| 6 | 1 | Main.java:8 | `Scanner` on System.in is never closed. | Note | Open it in a try-with-resources statement. |
| 7 | 1 | Main.java:11-13 | Input is not checked: a missing word or a non-number throws `ArrayIndexOutOfBoundsException` or `NumberFormatException` and ends the program. | Should fix | Check `words.length` and catch `NumberFormatException` before using the words. |
| 8 | 1 | ParcelService.java:56-61 | Magic numbers 3, 10.0, 15.0 and 20.0 for the free days and the daily rates. | Note | Name them as constants or put the rate on the size type. |
| 9 | 1 | ParcelService.java:28, 44, 68; Parcel.java:13 | Status values "NEW", "STORED", "PICKED_UP" are repeated string literals. | Should fix | Use an enum for the parcel status. |
| 10 | 2 | ParcelService.java:64 | `printParcels(boolean forStaff)` has logical cohesion: the flag chooses between two different reports. | Should fix | Split into two methods, one for staff and one for waiting parcels. |
| 11 | 2 | Main.java:19, 21 to ParcelService.java:64 | Control coupling: Main passes the flag `forStaff` that decides what `printParcels` does. | Should fix | Call two separate methods instead of passing a flag. |
| 12 | 2 | ParcelService.java:24-37 | `storeParcel` has sequential/communicational cohesion but also sends the SMS and saves the file, so ParcelService mixes locker rules, notification, console output and file saving (weak class cohesion). | Should fix | Move the SMS output to Main and the file saving into its own class. |
| 13 | 2 | ParcelService.java:74 | `saveToFile` gives ParcelService a second responsibility (persistence), so the class represents more than one thing. | Should fix | Move saving into a separate class such as ParcelFileStore. |
| 14 | 2 | Parcel.java:6, Locker.java:5, ParcelService.java:15-17, 26, 56-58, Main.java:13 | Connascence of meaning (convention): the size is an int code 1/2/3 that Parcel, Locker, ParcelService and Main must all read the same way. | Should fix | Replace the int with an enum `ParcelSize`. |
| 15 | 2 | ParcelService.java:28, 44, 68 | Connascence of meaning between ParcelService and Parcel on the status strings. | Should fix | Use an enum `ParcelStatus`. |
| 16 | 2 | Parcel.java:7 | Field `status` is public, and ParcelService changes it directly (common/content coupling on Parcel's data). | Must fix | Make it private and change it through a method. |
| 17 | 2 | ParcelService.java:20-22 | `getLockers()` returns the internal list, so callers can add or remove lockers. | Should fix | Return a count or an unmodifiable copy. |
| 18 | 2 | Main.java:22 | Main reaches through ParcelService to the Locker list and filters lockers itself (Law of Demeter, stamp coupling). | Should fix | Add `countFreeLockers()` to ParcelService. |
| 19 | 2 | ParcelService.java:52 | `fee(Parcel, int)` receives the whole Parcel but uses only its size (stamp coupling). | Note | Pass only the size. |
| 20 | 2 | Main.java:6-27 | `main` has procedural cohesion: it reads input, parses commands, builds objects and formats output in one method. | Note | Move each command into its own small method. |

3. Number of findings: Check 1: 9. Check 2: 11.
