import Data.List

-- Generate All Possible Substrings Also provide sorted version of them
allSubs :: String -> [(String,String)] 
allSubs x = [ (z, sort z) | i <- [0..length(x)-1], j<-[1..length(x)-i], z<-[(take j (drop i x))] ]

-- Group set of given strings a/c to second
groupSubsets :: [(String,String)] -> [[(String, String)]]
groupSubsets x = groupBy (\(_,a) (_,b) -> a==b) (sortOn snd x)

-- Main Function - Group subsets return all combinations
compute :: [String] -> (Int, [(String,String)])
compute s | length(s)/=2 = (0,[("Function accepts a list with only two `String` or `[Char]`.","")]) 
compute s = (length z, z)
    where subs = groupSubsets $ allSubs $ s!!0++s!!1
          z = [ ( fst $ x!!i , fst $ x!!j ) | x <- subs, i <- [0..length(x)-1], j <- [i+1..length(x)-1] ]