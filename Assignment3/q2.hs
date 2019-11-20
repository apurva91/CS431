import Data.List
import Data.String

allSubs :: String -> [(String,String)] 
allSubs x = [ (z, sort z) | i <- [0..length(x)-1], j<-[1..length(x)-i], z<-[(take j (drop i x))] ]

groupSubsets :: [(String,String)] -> [[(String, String)]]
groupSubsets x = groupBy (\(_,a) (_,b) -> a==b) (sortOn snd x)

get :: String -> String -> (Int, [(String,String)])
get s1 s2 = (length z, z)
    where subs = groupSubsets $ allSubs $ s1++s2
          z = [ ( fst $ x!!i , fst $ x!!j ) | x <- subs, i <- [0..length(x)-1], j <- [i+1..length(x)-1] ]