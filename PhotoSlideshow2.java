import java.io.*;
import java.util.*;

class PhotoSlideshow2 {
  public static void main (String [] args) throws IOException {
      //a_example
      //b_lovely_landscapes
      //c_memorable_moments
      //d_pet_pictures
      //e_shiny_selfies
    BufferedReader f = new BufferedReader(new FileReader("b_lovely_landscapes.txt"));
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("b_lovely_landscapes.out")));
    StringTokenizer st = new StringTokenizer(f.readLine());
    int n = Integer.parseInt(st.nextToken());
    
    List<Photo> vphotos = new ArrayList<>();
    List<Photo> photos = new ArrayList<>();
   
    for (int asdf = 0; asdf < n; asdf++) {
        st = new StringTokenizer(f.readLine());
        boolean vertical = st.nextToken().equals("V");
        int numtags = Integer.parseInt(st.nextToken());
        Set<String> tags = new HashSet<>();
        for (int i = 0; i < numtags; i++) {
            String tag = st.nextToken();
            tags.add(tag);
        }
        Photo p = new Photo(asdf, vertical? Photo.Type.VERTICAL : Photo.Type.HORIZONTAL, numtags, tags);
        if (vertical) vphotos.add(p);
        else photos.add(p);
    }
    
    Collections.sort(vphotos);
    
    /*for (Photo p : vphotos) System.out.println(p);
    System.out.println();
    
    for (Photo p : hphotos) System.out.println(p);
    System.out.println(); */
    
    for (int i = 0; i < vphotos.size(); i++) {
        Photo cur = vphotos.get(i);
        int bestDiff = -1;
        int index = -1;
        for (int j = i+1; j < vphotos.size(); j++) {
            Photo pot = vphotos.get(j);
            int same = cur.numSameTags(pot);
            int numDiff1 = cur.tags.size()-same;
            int numDiff2 = pot.tags.size()-same;
            int diff = numDiff1 + numDiff2;
            if (diff > bestDiff) {
                bestDiff = diff;
                index = j;
            }
        }
        Photo parent = new Photo(cur, vphotos.remove(index));
        vphotos.remove(i);
        i--;
        photos.add(parent);
    }
    
    Collections.sort(photos);
    /*
    for (Photo p : photos) {
        p.match = null;
        photos.remove(p)
        p.score = 0;
        for (Photo o : photos) {
            int sim = p.numOfSim(o); 
            int diff = p.numofDiff(o);
            int pdiff = p.tags.size() - sim;
            int Score = min(min(sim, diff), pdiff);

            if Score > p.score {
                p.match = o;
            }
        }

    } */
    //List<Photo> answer = new ArrayList<>();
    
    for (int i = 0; i < photos.size(); i++) {
        Photo p = photos.get(i);
        int index = -1;
        //photos.remove(p)
        int bestScore = -1;
        for (int k = 0; k < 37; k++) {
            int testIndex = (int) (Math.random() * (photos.size()-i));
            Photo o = photos.get(k);
            int sim = p.numSameTags(o); 
            int diff = p.numDifferentTags(o);
            int pdiff = p.tags.size() - sim;
            int Score = Math.min(Math.min(sim, diff), pdiff);
            if (Score > bestScore) {
                bestScore = Score;
                index = k;
            }
        }
        for (int j = i+1; j < photos.size(); j++) {
            
            Photo o = photos.get(j);
            int sim = p.numSameTags(o); 
            int diff = p.numDifferentTags(o);
            int pdiff = p.tags.size() - sim;
            int Score = Math.min(Math.min(sim, diff), pdiff);
            if (Score > bestScore) {
                bestScore = Score;
                index = j;
                break;
            }
        }
        if (index < 0) continue;
        //answer.add(p);
        //answer.add(photos.get(index));
        photos.add(i+1, photos.remove(index));

    }
    
    System.out.println(photos.size());
    for (Photo p : photos) {
        if (p.type.equals(Photo.Type.PARENT)) {
            System.out.println(p.left.id + " " + p.right.id);
        }
        else {
            System.out.println(p.id);
        }
    }
     
    out.close();
  }
  
}

class Photo implements Comparable<Photo> {
    Integer id;
    Type type;
    Integer numtags;
    Set<String> tags;
    Photo left;
    Photo right;
    
    public Photo(int i, Type v, int nt, Set<String> t) {
        id = i;
        type = v;
        numtags = nt;
        tags = t;
    }
    
    public Photo(Photo left, Photo right) {
        this.left = left;
        this.right = right;
        id = null;
        type = Type.PARENT;
        tags = new HashSet<>();
        tags.addAll(left.tags);
        tags.addAll(right.tags);
        numtags = tags.size();
    }

  @Override
  public int compareTo(Photo o) {
      return o.numtags.compareTo(numtags);
  }
  
  public int numDifferentTags(Photo o) {
      int ans = numtags;
      for (String s : tags) {
          if (o.tags.contains(s)) {
              ans--;
          }
      }
      return ans;
  }
  
  public int numSameTags(Photo o) {
      int ans = 0;
      for (String s : tags) {
          if (o.tags.contains(s)) {
              ans++;
          }
      }
      return ans;
  }
  
  
  
  @Override
  public String toString() {
      return id + " " + type + " " + numtags;
  }
  
  enum Type {
      HORIZONTAL, VERTICAL, PARENT
  }
}