# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/ZBWY10/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | [CHECK: first line of java -version] |
| Maven | [CHECK: first line of mvn -version] |
| IDE | Visual Studio Code [CHECK: version from Help > About] |
| SonarQube for IDE | [CHECK: version from the Extensions view] |
| AI tool and exact model | Claude (Cowork mode in the Claude desktop app), model claude-opus-5-5 |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: e77187d

Output of the starter for `store P1 6501 2`, `pickup P1 5`, `staff`: the fee is `0.0` although the parcel was kept 5 days, `staff` still shows `P1 6501 STORED` with 2 free lockers, so the pickup failed, and parcels.csv is empty.

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 2 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 3 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 4 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 5 | java:S108 | ParcelService.java:80 | Either remove or fill this block of code. |
| 6 | java:S1104 | Parcel.java:7 | Make status a static final constant or non-public and provide accessors if needed. |
| 7 | java:S1118 | Main.java:5 | Add a private constructor to hide the implicit public one. |
| 8 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 9 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 10 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 11 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 12 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 13 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 14 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 25 October 2026 |
| AI tool and exact model | Claude (Cowork), claude-opus-5-5 |
| Commit reviewed | e77187d |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | Attached the files (the starter zip was attached to the chat) |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | Strings compared with == | both | java:S4973 | yes, the run in C2 printed Fee: 0.0 because pickup never matched P1 |
| 2 | ParcelService.java:76 | FileWriter never closed | both | java:S2095 | yes, parcels.csv was empty after the run |
| 3 | ParcelService.java:80 | Empty catch block | both | java:S108 | yes, lines 80-81 contain nothing |
| 4 | ParcelService.java:6 | Unused import java.util.Date | both | java:S1128 | yes, Date is used nowhere in the file |
| 5 | ParcelService.java:12 | Unused field maxParcels | both | java:S1068 | yes, nothing reads maxParcels |
| 6 | Parcel.java:7 | Public field status | both | java:S1104 / encapsulation | yes, ParcelService writes parcel.status directly on lines 28 and 44 |
| 7 | Main.java:5 | Implicit public constructor in a class with only a static method | SonarQube | java:S1118 | yes, Main has only main |
| 8 | ParcelService.java:30, 67, 69; Main.java:9, 15, 17, 22 | System.out instead of a logger | SonarQube | java:S106 | yes for ParcelService, where printing is not the class's job; for Main it is the program's output |
| 9 | Main.java:8 | Scanner never closed | AI | resource leak | yes, but minor, because it wraps System.in for the whole run |
| 10 | Main.java:11-13 | Input not checked, a missing word crashes the program | AI | probable bug | yes, `store P1` alone throws ArrayIndexOutOfBoundsException |
| 11 | ParcelService.java:56-61 | Magic numbers for free days and rates | AI | code smell | yes, but only a note |
| 12 | ParcelService.java:28, 44, 68 | Status values as repeated strings | AI | connascence of meaning | yes, "STORED" must be spelled the same in two methods |
| 13 | ParcelService.java:64 | printParcels(boolean) chooses between two reports | AI | logical cohesion | yes, the two branches print different data |
| 14 | Main.java:19, 21 | Main passes the forStaff flag | AI | control coupling | yes, the flag decides what the callee does |
| 15 | ParcelService.java:24-37, 74 | ParcelService also sends the SMS, prints and saves the file | AI | weak class cohesion | yes, one class has locker rules, output and persistence |
| 16 | Parcel.java:6, Locker.java:5, ParcelService.java:15-17, 26, 56-58 | Size is an int code 1/2/3 read the same way by several classes | AI | connascence of meaning (convention) | yes, the meaning lives only in two comments |
| 17 | ParcelService.java:20-22 | getLockers returns the internal list | AI | exposed internal collection | yes, a caller can call clear() on it |
| 18 | Main.java:22 | Main reaches into the locker list to count free lockers | AI | strong coupling, Law of Demeter | yes, Main needs to know Locker::isFree |
| 19 | ParcelService.java:52 | fee receives the whole Parcel but uses only its size | AI | stamp coupling | yes, only getSize() is called |
| 20 | Main.java:6-27 | main reads, parses, builds and prints in one method | AI | procedural cohesion | partly; for a 20-line menu loop this is normal, so it is a note at most |

2.1 Name two problems that only the AI review found.

The control coupling of `printParcels(boolean forStaff)`, called from Main.java:19 and 21, and the connascence of meaning of the int size code 1/2/3 that Parcel, Locker and ParcelService must read in the same way. SonarQube has no rule for either design problem.

2.2 Name one problem that only SonarQube found, or write none.

java:S1118 at Main.java:5, the implicit public constructor of Main, which only has a static method. The AI review did not mention it.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

Finding 20 (procedural cohesion of `main`) is too strong: a command loop that reads a line and calls one service method per command is the normal shape of a small command-line program, and splitting it would not make it clearer. I checked every other finding by opening the file at the given line, and the two bugs by running the program: the fee was 0.0 and parcels.csv was empty, which matches findings 1 and 2.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Fixed | 9a8c282 |
| 2 | Fixed | c55894e |
| 3 | Fixed | 2446510 |
| 4 | Fixed | 8eeb5e2 |
| 5 | Fixed | 0a13101 |
| 6 | Fixed | 9104d70 |
| 7 | Fixed | 162171e |
| 8 | Fixed | 3a584c3 (the SMS line moved to Main) |
| 9 | Fixed | 3a584c3 (ParcelService now returns lines and Main prints them) |
| 10 | Fixed | 3a584c3 |
| 11 | Kept | Printing in Main is the output of a command-line program. |
| 12 | Kept | Printing in Main is the output of a command-line program. |
| 13 | Kept | Printing in Main is the output of a command-line program. |
| 14 | Kept | Printing in Main is the output of a command-line program. |

Number of SonarQube issues that remain after your fixes: 8, all java:S106 in Main.java (lines 12, 19, 21, 23, 26, 28, 30, 31), kept because they are the program's output. [CHECK: count in the Problems panel]

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Connascence of meaning: the parcel size is an int code 1/2/3 that Parcel, Locker, ParcelService and Main must read the same way | Added the enum ParcelSize (SMALL, MEDIUM, LARGE) with the fee per day and a fitsIn method; Parcel and Locker now hold a ParcelSize, and fee() no longer has an if chain | a57119c |
| Control coupling and logical cohesion: Main passed the flag forStaff to printParcels; ParcelService also printed to the console and exposed its locker list | Replaced printParcels(boolean) with allParcelLines() and waitingParcelLines(), which return lines that Main prints; moved the SMS output to Main; replaced getLockers() with countFreeLockers() | 3a584c3 |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
store P1 6501 2
SMS to 6501: parcel P1 is in locker 2
Locker 2
pickup P1 5
Fee: 30.0
staff
P1 6501 PICKED_UP
Free lockers: 3
quit

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 25 October 2026 | 25 October 2026 |
| AI tool and exact model | Claude (Cowork), claude-opus-5-5 | Claude (Cowork), claude-opus-5-5 |
| Prompt (B or C as given, or your changed prompt in full) | B as given | C as given |
| Builds with mvn package (yes or no) | yes [CHECK: run mvn package] | yes [CHECK: run mvn package] |
| Number of classes, interfaces, enums and records | 2 classes | 4 classes, 1 interface, 1 enum, 2 records |
| SonarQube issues, total | [CHECK] | [CHECK] |
| SonarQube issues, by rule | [CHECK, expected mostly java:S106, java:S1104/S1444 for the public static lists, java:S2301 for the flag, java:S1118] | [CHECK, expected java:S106 in Main only] |
| AI review findings in Check 1 | 5 | 2 |
| AI review findings in Check 2 | 9 | 3 |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

[CHECK and edit] SonarQube reported no rule named cohesion or coupling. Its closest issues were about the public static fields (java:S1104 / java:S1444, which point at common coupling) and, if it appears, java:S2301 "Provide multiple methods instead of using "rent" to determine which action to take", which is the control coupling of `process(boolean rent, ...)`. It did not see that BikeShare mixes the menu, the fee rules, the records and the file saving, or the connascence of position of the four parallel lists.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

Yes. The review of bikeshare-poor has 9 Check 2 findings, several marked Must fix, while bikeshare-good has 3 notes. For example, finding 8 for bikeshare-poor, the public static mutable lists at BikeShare.java:12-15 (common coupling), does not appear for bikeshare-good, whose review says that non-private fields were not found.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

bikeshare-good, because the change is one new line `VISITOR(4)` in the MemberType enum and the parser picks it up through MemberType.valueOf. In bikeshare-poor I would have to add a new int code, change the parsing in `handle` and the fee rule in `process`, and remember that 0 already means "nobody", which is connascence of meaning across the class.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

SonarQube checks code against rules for single lines and methods, such as the string comparison with == (java:S4973), the unclosed FileWriter (java:S2095) and the empty catch block (java:S108). It does not check the design between classes: it did not report the int size code shared by three classes or ParcelService doing four jobs. It also reports some issues that are fine in context, such as System.out in the Main of a command-line program.

5.2 When should you not trust the AI review? Give an example from this lab.

I should not trust it when it judges how serious a problem is or gives a cohesion level without a clear reason, because those are judgements, not facts. In this lab it called the menu loop in Main procedural cohesion, which is too strong for a small command loop. I should also check every line number, because a review can point at the wrong line, and check bug claims by running the code, as I did with the fee of 0.0.

5.3 What will you do differently in Project Phase 2 after this lab?

I will run SonarQube for IDE while I write the code, not only at the end, and fix the probable bugs first. I will run Prompt A for the design checks and verify each finding at its line before I put it in the report. I will avoid int codes and boolean flags between classes and use enums and separate methods instead.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Claude (Cowork), claude-opus-5-5, 25 October 2026 | Task 2: Prompt A review of parcelpoint | reviews/parcelpoint-review.md | Opened each file at each line given; ran the program to confirm the two bugs |
| Claude (Cowork), claude-opus-5-5, 25 October 2026 | Task 3: the fixes of the SonarQube issues and the two design fixes | parcelpoint/src (all files, and the new ParcelSize.java) | Built with mvn package after each fix and ran the C2 input; checked the Problems panel |
| Claude (Cowork), claude-opus-5-5, 25 October 2026 | Task 4: Prompts B and C, and Prompt A on both versions | bikeshare-poor/, bikeshare-good/, reviews/bikeshare-poor-review.md, reviews/bikeshare-good-review.md | Built both with mvn package; tried rentals, returns, a full station and bad input; checked the review lines |
| Claude (Cowork), claude-opus-5-5, 25 October 2026 | Drafting the report text, Table 1 from the expected SonarQube rules, and the answers to the questions | LAB_REPORT.md | Compared Table 1 with the SonarQube Problems panel and edited the answers |
