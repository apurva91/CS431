import Data.List 
import Data.Maybe

-- A seed value a/c to which array is shuffled
seed = 130
list = permutations ["BS","CM","CH","CV","CS","DS","EE","HU","MA","ME","PH","ST"]!!seed

-- Zipping two at a time
z = zip ([ list!!i | i<-[0,2..length(list)-1] ]) ([ list!!i | i<-[1,3..length(list)-1] ])

-- List of times in to be printed format and in decimal
times = [("1-11","9:30"),("1-11","7:30"),("2-11","9:30"),("2-11","7:30"),("3-11","9:30"),("3-11","7:30")]
times2 = [(1,9.5),(1,19.5),(2,9.5),(2,19.5),(3,9.5),(3,19.5)]

-- Tab space
tbsp = "    "

-- Helper Function to print matches
printMatch :: Int -> IO ()
printMatch i = putStrLn ((fst $ z!!i)  ++ tbsp ++ "vs" ++ tbsp ++ (snd $z!!i) ++ tbsp ++ (fst $ times!!i) ++ tbsp++tbsp ++ (snd $ times!!i))

-- For seeing the fixtures prints all fixtures or a team's fixture
fixture :: String -> IO ()
fixture "all" = do
    printMatch 0
    printMatch 1
    printMatch 2
    printMatch 3
    printMatch 4
    printMatch 5

fixture x = do
    if elem x list then
        printMatch ( (fromJust(elemIndex x list)) `div` 2 )
    else 
        print "Team Not Found."

-- Find Out next match given a time and date by seeing current element is bigger than which first index
nextMatch :: Int -> Float -> IO ()
nextMatch z w = do
    let p = ([ x | x<-[0..5], (z,w)<=(times2!!x) ])
    if length(p)>0 then printMatch (p!!0)
    else print "No matches Ahead."
