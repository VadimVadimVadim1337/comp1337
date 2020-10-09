 void main() {

        String str = "Hello World";
        String substr = "llo";
        int n = str.length();
        int i = 0;
        int index = -1;
        while (i < n) {
            //System.out.println(str.charAt(i));
            if(str.charAt(i) == substr.charAt(0)){
                int j = 0;
                index = i;
                while(j < substr.length()) {
                    if(str.charAt(i) != substr.charAt(j)) {
                        index = -1;
                        break;
                    }
                    j = j + 1;
                    i = i + 1;
                }
                if(index != -1){
                    break;
                }
            }
            i = i + 1;
        }
        System.out.println(index);
    }