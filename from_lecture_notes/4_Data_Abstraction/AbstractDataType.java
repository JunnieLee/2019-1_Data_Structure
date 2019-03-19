public class AbstractDataType {

    public class Sphere{
        private double theRadius;

        // default constructor
        public Sphere(){
            setRadius(1.0);
        }

        // constructor2 (overloading)
        public Sphere(double InitialRadius){
            setRadius(InitialRadius);
        }

        public void setRadius(double newRadius){
            if (newRadius >= 0.0){
                theRadius = newRadius;
            }
        }

        public double radius(){
            return theRadius;
        }

        public double volume(){
            return (4.0*Math.PI*Math.pow(theRadius, 3.0))/3.0;
        }

        public void displayStatistics(){
            System.out.println("\nRadius= "+radius());
            System.out.println("Volume= "+volume());
        }

        // over-riding
        public boolean equals(Object sp){
            return ((sp instanceof Sphere) && theRadius == ((Sphere)sp).radius());
        }

        // over-loading (diff parameter type)
        public boolean equals(Sphere sp){
            return (theRadius == sp.radius());
        }
    }

    // inheritance
    public class Ball extends Sphere{
        private String theName;

        // default constructor
        public Ball(){
            setName("unknown");
        }

        public Ball(double initialRadius, String initialName){
            super(initialRadius); // make use of the parent's constructor
            setName(initialName);
        }

        public String name(){
            return theName;
        }

        public void setName(String newName){
            theName = newName;
        }

        // over-riding
        public void displayStatistics(){
            System.out.println("\nStatistics for a " + name());
            super.displayStatistics();  // make use of the parent's method
        }
    }





    public static void main(String[] args) {
        System.out.println("2019-1 SNU Data Structure class by professor Moon! :) ");

    }
}
