1. verify on all devices
2. We should download reponse once and reuse it until app is open or for 24 hours (whichever is first) since we are grabbing all the ads at once and response provides 50 items 
3. Randomize the ads shown in banner and interstitial so it isnt always the first item
4. Exclude current app from the list if exists
5. Add proper documentation , readme and comments
6. the primary use of this app is fallback for admob ads when not availabe only then show ads from this library so keep that in mind and proceed accordingly
7. these are the api params 
GET
/api/promote

Public
Edge Cached
Public endpoint returning a curated, shuffled list of Elyte Labs apps and websites. Designed specifically for use as fallback promotional content inside Android apps when AdMob has no fill. Bypasses database entirely on repeat requests via 1-hour Edge caching.

Parameters
type
string
Filter results: "apps", "websites", or "all".
limit
number
Max items to return. Max: 50
exclude
string
Package ID or Website ID to exclude.
featured
boolean
If "true", returns only items marked as featured.