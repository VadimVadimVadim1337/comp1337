//dgfdfd
//dgf
 void main() {
        int a = 9;
        int b = 3;
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }

        System.out.println(a);
    }
